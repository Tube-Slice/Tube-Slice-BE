package TubeSlice.tubeSlice.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class ImageDto{
        private Long imageId;
        private String imageUrl;
    }
}
