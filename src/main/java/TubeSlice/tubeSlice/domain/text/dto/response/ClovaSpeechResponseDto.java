package TubeSlice.tubeSlice.domain.text.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClovaSpeechResponseDto {

    private String token;
    private String result;
    private String message;
}
