package TubeSlice.tubeSlice.domain.user.dto.oauth;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
