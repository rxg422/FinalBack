package com.jbro.tour.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.jbro.ai.model.dto.AIRecDto;
import com.jbro.ai.model.service.AIService;
import com.jbro.tour.model.service.LDongService;
import com.jbro.tour.model.service.TourApiService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TourDataRunner implements ApplicationRunner {

    private final TourApiService tourService;
    private final LDongService lDongService;
    private final AIService aiService;

    @Override
    public void run(ApplicationArguments args) {
        try {
//        	System.out.println("🚀 서버 시작 - 관광 데이터 수집 시작");
//            tourService.fetchAndSaveTourData();
//
//            System.out.println("🚀 서버 시작 - 시군구 데이터 수집 시작");
//            lDongService.fetchAndSaveLDongData();
        	
//        	System.out.println("AI 관광지 추천 테스트");
//        	List<AIRecDto> list = aiService.aiRecommend();
//        	System.out.println(list);
        	
        	
        }
        catch (Exception e) {
            System.out.println("❌ 데이터 수집 실패");
            e.printStackTrace();
        }
       


        System.out.println("✅ 데이터 수집 완료");
    }
}