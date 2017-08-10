package hello.token.caching;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Profile {
    private String name;
    private String address;
}
