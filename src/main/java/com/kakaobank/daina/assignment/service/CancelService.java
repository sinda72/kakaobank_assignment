package com.kakaobank.daina.assignment.service;

import com.kakaobank.daina.assignment.domain.AccInfo;
import com.kakaobank.daina.assignment.domain.HistorySimTransDetail;
import com.kakaobank.daina.assignment.domain.SimTransDetail;
import com.kakaobank.daina.assignment.exception.BizException;
import com.kakaobank.daina.assignment.mapper.AccInfoMapper;
import com.kakaobank.daina.assignment.mapper.CancelTarMapper;
import com.kakaobank.daina.assignment.mapper.HistorySimTransDetailMapper;
import com.kakaobank.daina.assignment.mapper.SimTransDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CancelService {

    private final SimTransDetailMapper simTransDetailMapper;
    private final CancelTarMapper cancelTarMapper;
    private final VerificationService verificationService;
    private final AccountingService accountingService;
    private final HistorySimTransDetailMapper historySimTransDetailMapper;
    private final AccInfoMapper accInfoMapper;

    public CancelService(AccInfoMapper accInfoMapper, HistorySimTransDetailMapper historySimTransDetailMapper, AccountingService accountingService, VerificationService verificationService, SimTransDetailMapper simTransDetailMapper, CancelTarMapper cancelTarMapper) {
        this.simTransDetailMapper = simTransDetailMapper;
        this.cancelTarMapper = cancelTarMapper;
        this.verificationService =  verificationService;
        this.accountingService = accountingService;
        this.historySimTransDetailMapper = historySimTransDetailMapper;
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
    public void cancelMoney(Long tId, String tCode) {
        SimTransDetail byId = simTransDetailMapper.findById(tId);

        //??????????????????, ?????????????????? ??????
        boolean checkCode  = verificationService.checkCode(byId, "C1");

        //?????? ?????? ??????_????????????????????????_update
        byId.editTcode(tCode);
        simTransDetailMapper.updatetCode(byId);

        //?????? ?????? ??????
        AccInfo account = accInfoMapper.findBaccAll(byId.getAccId());
        if(account == null){
            throw new BizException("??????????????? ???????????? ????????????.");
        }
        if(!(account.getBaccStatus().equals("NORMAL") | account.getBaccStatus().equals("normal"))){
            throw new BizException("????????? ???????????? ???????????????.");
        }

        //?????? ????????????
        account.editMoney(account.getBaccBalance()+byId.gettAmount());
        accInfoMapper.update(account);

        //?????????????????? select?????? ??????????????????, ???????????? ????????????
        verificationService.checkCancelCode(byId, "??????");

        //???????????? ?????? ??????
        byId.editTcode(tCode);
        simTransDetailMapper.updatetCode(byId);

        //???????????? insert
        HistorySimTransDetail historySimTransDetail = HistorySimTransDetail.createNew(byId.gettId(),
                byId.getAccId(),
                byId.getCtmId(),
                byId.getrName(),
                byId.getAccId(),
                byId.getReKkoUid(),
                byId.gettAmount(),
                byId.getCommission(),
                LocalDate.now(),
                LocalTime.now(),
                byId.gettCode());
        historySimTransDetailMapper.insert(historySimTransDetail);

        //??????????????????
        accountingService.accountingTransfer(byId, historySimTransDetail, tCode);
    }
}
