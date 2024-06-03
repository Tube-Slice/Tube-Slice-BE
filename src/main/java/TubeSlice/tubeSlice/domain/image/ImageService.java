package TubeSlice.tubeSlice.domain.image;

import TubeSlice.tubeSlice.domain.image.dto.response.ImageResponseDto;
import TubeSlice.tubeSlice.domain.user.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Transactional
    public ImageResponseDto.ImageDto uploadImage(MultipartFile file, User user) {

        ImageResponseDto.S3Dto s3Result = s3Service.uploadFile(file);

        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .url(s3Result.getFileUrl())
                .type(file.getContentType())
                .user(user)
                .build();

        imageRepository.save(image);

        return ImageConverter.toImageDto(image);
    }
}
