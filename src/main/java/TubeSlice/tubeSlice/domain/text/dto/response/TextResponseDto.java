package TubeSlice.tubeSlice.domain.text.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextResponseDto {

    private Double timeline;
    private String scripts;

}
