package com.kakaobank.daina.assignment.service;

import com.kakaobank.daina.assignment.domain.ReceiHis;
import com.kakaobank.daina.assignment.domain.SimTransDetail;
import com.kakaobank.daina.assignment.dto.CreateRnameIn;
import com.kakaobank.daina.assignment.mapper.ReceiHisMapper;
import com.kakaobank.daina.assignment.mapper.SimTransDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class ReceiverService {

    private final SimTransDetailMapper simTransDetailMapper;
    private final ReceiHisMapper receiHisMapper;


    public ReceiverService(SimTransDetailMapper simTransDetailMapper, ReceiHisMapper receiHisMapper) {

        this.simTransDetailMapper = simTransDetailMapper;
        this.receiHisMapper = receiHisMapper;
    }

    public List<ReceiHis> findTransactions() {
        List<ReceiHis> receiHis = receiHisMapper.findAll();
        return receiHis;
    }

    @Transactional
    public SimTransDetail createReceiver(CreateRnameIn createRnameIn) {
        //form에 입력된 값 SimTransDetail 객체에 대입
        SimTransDetail simTransDetail = SimTransDetail.createNew(
                createRnameIn.getrName(),
                createRnameIn.getrNick(),
                createRnameIn.getReKkoUid()
        );
        //객체 넘겨 mapper통해서 db에 값 insert
        simTransDetailMapper.insert(simTransDetail);

        return simTransDetail;
    }
}
