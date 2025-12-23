package xyz.jdynb.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryArtistMusicParams extends PageParams {

    private Long artistId;
}
