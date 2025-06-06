package com.backend.bilibili.service.profile;


import com.backend.bilibili.service.dto.VideoCardDetailDTO;

import java.util.List;

public interface DynamicService {
    List<VideoCardDetailDTO> getUserDynamics();
}
