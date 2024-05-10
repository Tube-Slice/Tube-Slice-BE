package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {
}
