package com.example.pehd.service;

import com.example.pehd.dto.PlaygroundCoordinateDto;
import com.example.pehd.dto.PlaygroundDropdownDto;
import com.example.pehd.entity.PlaygroundCoordinate;
import com.example.pehd.repository.PlaygroundCoordinateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 操场坐标服务层
 */
@Service
public class PlaygroundCoordinateService {

    private static final Logger logger = LoggerFactory.getLogger(PlaygroundCoordinateService.class);

    @Autowired
    private PlaygroundCoordinateRepository playgroundCoordinateRepository;

    /**
     * 根据学校和操场名称查询坐标
     */
    public PlaygroundCoordinateDto getCoordinateBySchoolAndPlayground(String school, String playgroundName) {
        logger.info("查询坐标: 学校={}, 操场={}", school, playgroundName);

        Optional<PlaygroundCoordinate> result = playgroundCoordinateRepository
                .findBySchoolAndPlaygroundName(school, playgroundName);

        if (result.isPresent()) {
            PlaygroundCoordinate coordinate = result.get();
            return new PlaygroundCoordinateDto(
                    coordinate.getSchool(),
                    coordinate.getPlaygroundName(),
                    coordinate.getLongitude1(),
                    coordinate.getLatitude1(),
                    coordinate.getLongitude2(),
                    coordinate.getLatitude2()
            );
        } else {
            throw new RuntimeException("未找到指定地点的坐标信息: " + school + " - " + playgroundName);
        }
    }

    /**
     * 获取所有学校和操场的下拉列表数据
     */
    public List<PlaygroundDropdownDto> getPlaygroundDropdown() {
        logger.info("获取所有学校和操场下拉列表");

        // 获取所有不同的学校
        List<String> schools = playgroundCoordinateRepository.findAllDistinctSchools();

        // 为每个学校获取对应的操场列表
        return schools.stream()
                .map(school -> {
                    List<PlaygroundCoordinate> playgrounds = playgroundCoordinateRepository
                            .findBySchoolOrderByPlaygroundName(school);

                    List<PlaygroundDropdownDto.PlaygroundInfo> playgroundInfos = playgrounds.stream()
                            .map(coordinate -> new PlaygroundDropdownDto.PlaygroundInfo(coordinate.getPlaygroundName()))
                            .collect(Collectors.toList());

                    return new PlaygroundDropdownDto(school, playgroundInfos);
                })
                .collect(Collectors.toList());
    }
}