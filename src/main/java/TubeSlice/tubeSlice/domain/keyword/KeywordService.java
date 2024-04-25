package TubeSlice.tubeSlice.domain.keyword;

import TubeSlice.tubeSlice.domain.keyword.dto.response.KeywordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Transactional
    public KeywordResponseDto.KeywordResultDto createPostKeyword(String keyword){

        boolean exist = keywordRepository.existsByName(keyword);

        Keyword keyword1 = null;

        if(!exist){
            keyword1 = Keyword.builder()
                    .name(keyword)
                    .build();
            keywordRepository.save(keyword1);
        }else{
            keyword1 = keywordRepository.findByName(keyword);
        }

        return KeywordResponseDto.KeywordResultDto.builder()
                .keywordId(keyword1.getId())
                .build();
    }
}
