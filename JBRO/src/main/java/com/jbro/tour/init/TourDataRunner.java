package com.jbro.tour.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.jbro.tour.model.service.LDongService;
import com.jbro.tour.model.service.TourApiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TourDataRunner implements ApplicationRunner {

    private final TourApiService tourService;
    private final LDongService lDongService;

    @Override
    public void run(ApplicationArguments args) {
        try {

//        	System.out.println("🚀 서버 시작 - 관광 데이터 수집 시작");
//            tourService.fetchAndSaveTourData();

//            System.out.println("🚀 서버 시작 - 시군구 데이터 수집 시작");
//            lDongService.fetchAndSaveLDongData();
        }
        catch (Exception e) {
           // System.out.println("❌ 데이터 수집 실패");
          //  e.printStackTrace();
        }
       


        System.out.println("✅ 데이터 수집 완료");
    }
}