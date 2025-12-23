package xyz.jdynb.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

import java.util.Date;

import lombok.Data;

@Schema(description = "主页数据")
@Data
@Accessors(chain = true)
public class HomeDataVO {

    @Schema(description = "banner 数据")
    private List<Banner> banners;

    @Schema(description = "歌单")
    private Playlist playlist;

    @Schema(description = "歌单分类标签列表")
    private List<PlaylistTag> playlistTag;

    // private List<ArtistTagBean> artistTag;

    // private List<?> activity;

    @Schema(description = "排行榜列表数据")
    private List<Rank> bang;

    private Radio radio;

    // private String canonicalHref;


    @Data
    @Accessors(chain = true)
    public static class Banner {

        private String newPic;

        private String newPicText;

        private Long id;

        private String pic;

        private Integer priority;

        private String newPicTag;

        private String url;

    }

    @Data
    @Accessors(chain = true)
    public static class Playlist {

        private List<PlayListItem> list;


        @Data
        @Accessors(chain = true)
        public static class PlayListItem {

            private String img;

            private String uname;

            private String img700;

            private String img300;

            private String userName;

            private String img500;

            private Integer total;

            private String name;

            private Integer listencnt;

            private Long id;

            private List<?> musicList;

            private String desc;

            private String info;

        }
    }

    @Data
    @Accessors(chain = true)
    public static class PlaylistTag {

        private String name;

        private String id;

    }

    @Data
    @Accessors(chain = true)
    public static class ArtistTagBean {

        private String name;

        private Long id;

    }

    @Data
    @Accessors(chain = true)
    public static class Rank {

        private String leader;

        private Long num;

        private String name;

        private String pic;

        private Long id;

        private Date pub;

        private List<MusicListBean> musicList;


        @Data
        @Accessors(chain = true)
        public static class MusicListBean {

            private String musicrid;

            private Long barrage;

            private String adType;

            private String artist;

            private Mvpayinfo mvpayinfo;

            private String pic;

            private Integer isstar;

            private Long rid;

            private Integer duration;

            private Long score100;

            private Long adSubtype;

            private Long contentType;

            private Integer track;

            private Integer hasmv;

            private Date releaseDate;

            private String album;

            private Long albumid;

            private Long pay;

            private Long artistid;

            private String albumpic;

            private Integer originalsongtype;

            private Boolean isListenFee;

            private String pic120;

            private String name;

            private Integer online;

            private PayInfo payInfo;

            private Long tmeMusicianAdtype;


            @Data
            @Accessors(chain = true)
            public static class Mvpayinfo {

                private Integer play;

                private Integer vid;

                private Integer down;

            }

            @Data
            @Accessors(chain = true)
            public static class PayInfo {

                private Long play;

                private Long nplay;

                private Long overseasNplay;

                private Long localEncrypt;

                private Integer limitfree;

                private Integer refrainStart;

                private FeeType feeType;

                private Long down;

                private Long ndown;

                private Long download;

                private Integer cannotDownload;

                private Long overseasNdown;

                private Long listenFragment;

                private Integer refrainEnd;

                private Integer cannotOnlinePlay;

                private Integer paytype;

                private Paytagindex paytagindex;


                @Data
                @Accessors(chain = true)
                public static class FeeType {

                    private Long song;

                    private Long vip;

                }

                @Data
                @Accessors(chain = true)
                public static class Paytagindex {

                    private Integer s;

                    private Integer f;

                    private Integer zp;

                    private Integer h;

                    private Integer zPGA201;

                    private Integer zply;

                    private Integer hr;

                    private Integer l;

                    private Integer zPGA501;

                    private Integer db;

                    private Integer aR501;

                }
            }
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Radio {

        private List<Album> albumList;


        @Data
        @Accessors(chain = true)
        public static class Album {

            private String artist;

            private String album;

            private Long listencnt;

            private String pic;

            private Long rid;

        }
    }
}