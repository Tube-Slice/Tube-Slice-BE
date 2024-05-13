package TubeSlice.tubeSlice.domain.text.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SummaryRequestDto {

    private String row;
    private String script;
}
