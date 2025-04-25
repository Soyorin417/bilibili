<template>
  <div class="video-actions d-flex align-items-center mt-3 pb-3 border-bottom">
    <div class="d-flex align-items-center me-4">
      <div class="me-2" @click="toggleLike">
        <thumbs-up v-if="!videoInfo.is_like" theme="outline" size="27" fill="#000000" />
        <thumbs-up v-if="videoInfo.is_like" theme="filled" size="27" fill="#00aeec" />
        {{ videoInfo.likes }}
      </div>
      <div class="me-2" @click="toggleDislike">
        <thumbs-down
          v-if="!videoInfo.is_dislike"
          theme="outline"
          size="27"
          fill="#000000"
        />
        <thumbs-down
          v-if="videoInfo.is_dislike"
          theme="filled"
          size="27"
          fill="#00aeec"
        />
        {{ videoInfo.dislikes }}
      </div>
    </div>
    <div class="me-3" @click="toggleCollect">
      <star
        v-if="!videoInfo.is_collect"
        theme="outline"
        size="27"
        fill="#000000"
        :strokeWidth="3"
        strokeLinecap="square"
      />
      <star
        v-if="videoInfo.is_collect"
        theme="filled"
        size="27"
        fill="#00aeec"
        :strokeWidth="3"
        strokeLinecap="square"
      />
      收藏 {{ videoInfo.collects }}
    </div>
    <div class="me-3" @click="toggleShare">
      <share-two
        v-if="!videoInfo.is_share"
        theme="outline"
        size="27"
        fill="#000000"
        :strokeWidth="3"
        strokeLinecap="square"
      />
      <share-two
        v-if="videoInfo.is_share"
        theme="filled"
        size="27"
        fill="#00aeec"
        :strokeWidth="3"
        strokeLinecap="square"
      />分享 {{ videoInfo.shares }}
    </div>
  </div>
</template>

<script>
import { ThumbsUp, ThumbsDown, Star, ShareTwo } from "@icon-park/vue-next";

export default {
  name: "VideoActions",
  components: {
    ThumbsUp,
    ThumbsDown,
    Star,
    ShareTwo,
  },
  props: {
    videoInfo: {
      type: Object,
      required: true,
    },
  },
  emits: ["update:videoInfo"],
  methods: {
    toggleLike() {
      this.$emit("update:videoInfo", {
        ...this.videoInfo,
        is_like: !this.videoInfo.is_like,
      });
    },
    toggleDislike() {
      this.$emit("update:videoInfo", {
        ...this.videoInfo,
        is_dislike: !this.videoInfo.is_dislike,
      });
    },
    toggleCollect() {
      this.$emit("update:videoInfo", {
        ...this.videoInfo,
        is_collect: !this.videoInfo.is_collect,
      });
    },
    toggleShare() {
      this.$emit("update:videoInfo", {
        ...this.videoInfo,
        is_share: !this.videoInfo.is_share,
      });
    },
  },
};
</script>

<style scoped>
.video-actions {
  user-select: none;
}

.video-actions > div {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #505050;
  font-size: 14px;
}

.video-actions > div:hover {
  color: #00aeec;
}
</style>
