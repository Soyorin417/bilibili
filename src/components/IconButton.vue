<template>
  <div>
    <div class="rounded-circle" :style="{ backgroundColor: color }">
      <a href="#" role="button" aria-expanded="false">
        <component
          :is="icon"
          theme="multi-color"
          size="24"
          :fill="['#ffffff', color, '#ffffff', color]"
        />
      </a>
    </div>
    <div class="rounded-circle text-center">{{ label }}</div>
  </div>
</template>

<script>
import { WindmillTwo, Fire } from "@icon-park/vue-next";

export default {
  name: "IconButton",
  components: {
    WindmillTwo,
    Fire,
  },
  props: {
    icon: {
      type: String,
      required: true,
    },
    color: {
      type: String,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
  },
};
</script>
<style scoped>
.rounded-circle {
  width: 3em;
  height: 3em;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: small;
  border-radius: 50%;
}
</style>
