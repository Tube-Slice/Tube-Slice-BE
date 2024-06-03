package TubeSlice.tubeSlice.domain.image;

import TubeSlice.tubeSlice.domain.image.dto.response.ImageResponseDto;

public class ImageConverter {
    public static ImageResponseDto.ImageDto toImageDto(Image image){
        return ImageResponseDto.ImageDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getUrl())
                .build();
    }
}
