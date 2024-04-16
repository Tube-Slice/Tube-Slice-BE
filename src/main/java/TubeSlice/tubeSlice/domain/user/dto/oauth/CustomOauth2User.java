package TubeSlice.tubeSlice.domain.user.dto.oauth;

import TubeSlice.tubeSlice.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class CustomOauth2User implements OAuth2User {


    private final User user;
    private Map<String,Object> attributes;

    public CustomOauth2User(User user, Map<String, Object> attributes){
        this.user=user;
        this.attributes=attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
            return this.user.getLoginId();
    }

    public String getProvider() {return this.user.getSocialType(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }


}

