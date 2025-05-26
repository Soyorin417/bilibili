<template>
  <div>
    <h4 class="title-wrapper">
      {{ title }}
    </h4>
    <div class="stats-wrapper d-flex align-items-center">
      <play-two
        theme="outline"
        size="16"
        fill="#666666"
        :strokeWidth="2"
        class="me-1 icon-fix"
      />
      <span class="me-3 stats-text">{{ formattedViews }}</span>
      <comment-one
        theme="outline"
        size="16"
        fill="#666666"
        :strokeWidth="2"
        class="me-1 icon-fix"
      />
      <span class="me-3 stats-text">{{ formattedComments }}</span>
      <span class="me-3 stats-text">{{ formattedTime }}</span>
      <span v-if="!showRight" class="ms-2">
        <forbid theme="outline" size="16" fill="#fc0404" class="me-1 icon-fix" />
        <span class="copyright-text">未经作者授权，禁止转载</span>
      </span>
    </div>
  </div>
</template>

<script>
import { PlayTwo, CommentOne, Forbid } from "@icon-park/vue-next";
export default {
  name: "VideoStats",
  components: { PlayTwo, CommentOne, Forbid },
  props: {
    title: String,
    formattedViews: String,
    formattedComments: String,
    formattedTime: String,
    showRight: {
      type: Boolean,
      default: true,
    },
  },
};
</script>
<style scoped>
.stats-wrapper {
  margin-top: 12px;
  margin-bottom: 12px;
  line-height: 1;
}

.stats-text {
  font-size: 13px;
  color: #666666;
}

.icon-fix {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  vertical-align: middle;
}
.copyright-text {
  font-size: 12px;
  color: #666666;
}
</style>
