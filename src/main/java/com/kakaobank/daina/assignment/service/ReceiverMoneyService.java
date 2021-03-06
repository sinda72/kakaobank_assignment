package com.kakaobank.daina.assignment.service;

import com.kakaobank.daina.assignment.domain.AccInfo;
import com.kakaobank.daina.assignment.domain.HistorySimTransDetail;
import com.kakaobank.daina.assignment.domain.SimTransDetail;
import com.kakaobank.daina.assignment.dto.ReceiveMoneyIn;
import com.kakaobank.daina.assignment.exception.BizException;
import com.kakaobank.daina.assignment.mapper.AccInfoMapper;
import com.kakaobank.daina.assignment.mapper.CancelTarMapper;
import com.kakaobank.daina.assignment.mapper.HistorySimTransDetailMapper;
import com.kakaobank.daina.assignment.mapper.SimTransDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReceiverMoneyService {

    private final SimTransDetailMapper simTransDetailMapper;
    private final VerificationService verificationService;
    private final AccountingService accountingService;
    private final HistorySimTransDetailMapper historySimTransDetailMapper;
    private final CancelTarMapper cancelTarMapper;
    private final AccInfoMapper accInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(ReceiverMoneyService.class);

    public ReceiverMoneyService(AccInfoMapper accInfoMapper, CancelTarMapper cancelTarMapper, SimTransDetailMapper simTransDetailMapper, VerificationService verificationService, AccountingService accountingService, HistorySimTransDetailMapper historySimTransDetailMapper) {
        this.simTransDetailMapper = simTransDetailMapper;
        this.verificationService = verificationService;
        this.accountingService = accountingService;
        this.historySimTransDetailMapper = historySimTransDetailMapper;
        this.cancelTarMapper = cancelTarMapper;
        this.accInfoMapper = accInfoMapper;
    }

    public List<SimTransDetail> findTransactions(String reKkoUid) {
        List<SimTransDetail> simTransDetails = simTransDetailMapper.findSend(reKkoUid);

        return simTransDetails;
    }
    public SimTransDetail findInformation(Long tId) {
        SimTransDetail simTransDetail = simTransDetailMapper.findById(tId);

        return simTransDetail;
    }

    @Transactional
    public void receiveMoney(ReceiveMoneyIn receiveMoneyIn) {
        //?????????????????? ??????
        SimTransDetail byId = simTransDetailMapper.findById(receiveMoneyIn.gettId());

        //??????????????????, ?????????????????? ??????
        boolean checkCode  = verificationService.checkCode(byId, "C1");

        //????????? ?????? ?????? ??????
        AccInfo accInfo = accInfoMapper.findBaccAll(receiveMoneyIn.getrAccId());
        if(accInfo == null){
            throw new BizException("??????????????? ???????????? ????????????.");
        }
        if(!accInfo.getCtmName().equals(byId.getrName())){
            throw new BizException("????????? ???????????? ????????????.");
        }

        //???????????????????????? ??????
        verificationService.checkCancelCode(byId, "??????");

        //????????????????????????
        accInfo.editMoney(accInfo.getBaccBalance()+byId.gettAmount());
        accInfoMapper.update(accInfo);

        HistorySimTransDetail historySimTransDetail = updateTransfer(receiveMoneyIn, byId);

        accountingService.accountingTransfer(byId, historySimTransDetail, "C3");
    }
    private HistorySimTransDetail updateTransfer(ReceiveMoneyIn receiveMoneyIn, SimTransDetail byId) {
        //???????????????????????? ????????????
        byId.editTcode("C3");
        simTransDetailMapper.updatetCode(byId);

        //???????????? insert
        HistorySimTransDetail historySimTransDetail = HistorySimTransDetail.createNew(byId.gettId(),
                byId.getAccId(),
                byId.getCtmId(),
                byId.getrName(),
                receiveMoneyIn.getrAccId(),
                byId.getReKkoUid(),
                byId.gettAmount(),
                byId.getCommission(),
                LocalDate.now(),
                LocalTime.now(),
                byId.gettCode());
        historySimTransDetailMapper.insert(historySimTransDetail);

        return historySimTransDetail;
    }

}
