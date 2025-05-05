<template>
  <div>
    Pdd

    <button @click="getPddData">获取拼多多数据</button>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "PddView",
  methods: {
    getPddData() {
      const url = "http://127.0.0.1:8081/pdd/getPddData";
      const token = localStorage.getItem("token");
      console.log(token, "token");
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      axios
        .get(url, { headers })
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.error("请求失败：", err);
        });
    },
  },
};
</script>

<style scoped></style>
