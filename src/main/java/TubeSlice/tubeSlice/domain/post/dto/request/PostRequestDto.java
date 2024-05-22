package TubeSlice.tubeSlice.domain.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class PostRequestDto {


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateDto{
        private String title;
        private String content;
        private String youtubeUrl;
        private List<String> postKeywords;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateDto{
        private String title;
        private String content;
        private String youtubeUrl;
        private List<String> postKeywords;
    }
}
