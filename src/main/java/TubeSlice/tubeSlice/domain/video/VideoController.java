package TubeSlice.tubeSlice.domain.video;

import TubeSlice.tubeSlice.domain.video.dto.ClovaSpeechResponseDto;
import TubeSlice.tubeSlice.domain.video.dto.VideoResponseDto;
import TubeSlice.tubeSlice.global.response.ApiResponse;
import com.amazonaws.services.dynamodbv2.xspec.NULL;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/translation")
    @Operation(summary = "영상에서 스크립트 추출", description = "유튜브 url을 입력 받아 영상의 스크립트를 반환한다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER401", description = "변환에 실패하였습니다.",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<Object> videoToScript(@RequestParam("fileName") String wavFile){



        return ApiResponse.onSuccess(videoService.videoToScript(wavFile));
    }

    @PostMapping("/upload")
    public ApiResponse<ClovaSpeechResponseDto> uploadFile(@RequestParam("filePath") String filePath){

        videoService.uploadFile(filePath);

        return ApiResponse.onSuccess(new ClovaSpeechResponseDto());
    }

    @GetMapping("/script")
    public ApiResponse<Object> getScript(@RequestParam("fileName") String fileName){


        return ApiResponse.onSuccess(videoService.getScriptFromBucket(fileName));

    }

//    @PostMapping("/youtubeUrl")
//    public ApiResponse<Object> youtubeUrl(@RequestParam("youtubeUrl") String youtubeUrl){
//
//
//    }


}
