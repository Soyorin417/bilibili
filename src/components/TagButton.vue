<template>
  <div :style="{ background: color }">
    <a href="#" role="button" aria-expanded="false">
      <component :is="icon" theme="filled" size="20" fill="#61676d"
    /></a>
    <span class="text-center" style="margin-left: 8px">{{ label }}</span>
  </div>
</template>

<script>
import { Message, Flag, Comments, Monitor, BookOpen, Music } from "@icon-park/vue-next";
export default {
  name: "TagButton",
  components: { Message, Flag, Comments, Monitor, BookOpen, Music },
  props: {
    icon: {
      type: String,
      require: true,
    },
    label: { type: String, require: true },
  },
};
</script>

<style scoped>
span {
  font-size: small;
}
</style>
