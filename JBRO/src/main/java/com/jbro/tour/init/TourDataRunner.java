package com.jbro.tour.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import com.jbro.tour.model.service.TourApiService;

@Component
@RequiredArgsConstructor
public class TourDataRunner implements ApplicationRunner {

    private final TourApiService tourService;

    @Override
    public void run(ApplicationArguments args) {

        System.out.println("🚀 서버 시작 - 관광 데이터 수집 시작");

        try {
            tourService.fetchAndSaveTourData();
        } catch (Exception e) {
            System.out.println("❌ 데이터 수집 실패");
            e.printStackTrace();
        }

        System.out.println("✅ 데이터 수집 완료");
    }
}