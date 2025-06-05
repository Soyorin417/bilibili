<template>
  <div class="card-body">
    <div class="row">
      <!--左边区域-->
      <div class="col-1 mt-1">
        <div class="row">
          <div class="col-6">
            <router-link
              to="/activity"
              class="nav-link"
              :class="{ active: isActive('/activity') }"
            >
              <IconButton icon="WindmillTwo" color="#ff9212" label="动态" />
            </router-link>
          </div>
          <div class="col-5">
            <router-link to="/hot" class="nav-link" :class="{ active: isActive('/hot') }">
              <IconButton icon="Fire" color="#f07775" label="热门" />
            </router-link>
          </div>
          <div class="col-1"></div>
        </div>
      </div>

      <!--中间tag区域-->
      <div class="col-9">
        <div class="row">
          <div v-for="(tag, index) in tags" :key="index" class="col-1">
            <div
              v-for="(item, i) in tag"
              :key="i"
              class="card tag border border-0"
              :class="{ 'mt-1': i > 0, active: isActive(`/${item.link.toLowerCase()}`) }"
            >
              <router-link class="card-body fw-normal nav-link" :to="item.link">{{
                item.name
              }}</router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧空白区域 -->
      <div class="col-2 mt-1">
        <div class="row border-start">
          <div v-for="(tag, index) in main_tags" :key="index" class="col-4">
            <div v-for="(item, i) in tag" :key="i" :class="{ 'mt-4': i > 0 }">
              <router-link
                class="main_tag nav-link"
                :class="{ active: isActive(`/${item[2].toLowerCase()}`) }"
                :to="item[2]"
              >
                <TagButton :icon="item[0]" :label="item[1]" />
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TagButton from "@/components/content/homeView/TagButton.vue";
import IconButton from "@/components/content/homeView/IconButton.vue";

export default {
  name: "TopNav",
  components: {
    IconButton,
    TagButton,
  },
  data() {
    return {
      tags: [
        [
          { name: "番剧", link: "Anime" },
          { name: "电影", link: "Movie" },
        ],
        [
          { name: "国创", link: "Original" },
          { name: "电视剧", link: "Drama" },
        ],
        [
          { name: "综艺", link: "Show" },
          { name: "纪录片", link: "Doc" },
        ],
        [
          { name: "动画", link: "Cartoon" },
          { name: "游戏", link: "Game" },
        ],
        [
          { name: "鬼畜", link: "Remix" },
          { name: "音乐", link: "Music" },
        ],
        [
          { name: "舞蹈", link: "Dance" },
          { name: "影视", link: "More" },
        ],
        [
          { name: "娱乐", link: "Fun" },
          { name: "知识", link: "Study" },
        ],
        [
          { name: "数码科技", link: "Tech" },
          { name: "资讯", link: "News" },
        ],
        [
          { name: "美食", link: "Food" },
          { name: "小剧场", link: "Stage" },
        ],
        [
          { name: "汽车", link: "Auto" },
          { name: "时尚美妆", link: "Style" },
        ],
        [
          { name: "体育运动", link: "Sport" },
          { name: "动物", link: "Pet" },
        ],
        [
          { name: "vlog", link: "Vlog" },
          { name: "更多", link: "More" },
        ],
      ],
      main_tags: [
        [
          ["Message", "专栏", "Column"],
          ["Monitor", "直播", "Broadcast"],
        ],
        [
          ["Flag", "活动", "Activity"],
          ["BookOpen", "课堂", "Classroom"],
        ],
        [
          ["Comments", "社区中心", "CommunityCenters"],
          ["Music", "新歌热榜", "NewSongs"],
        ],
      ],
    };
  },
  methods: {
    isActive(path) {
      return this.$route.path.toLowerCase() === path.toLowerCase();
    },
    getActivityTag() {
      return this.tags[0][0].link;
    },
  },
};
</script>

<style scoped>
.tag {
  background-color: #f6f7f8;
  font-size: small;
  width: 88px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 5px;
  transition: background-color 0.3s;
}

.tag:hover {
  background: #e3e5e7;
}

.tag.active {
  background-color: #e3e5e7;
  font-weight: bold;
}

.main_tag {
  width: 88px;
}

.nav-link {
  color: inherit;
  text-decoration: none;
}

.nav-link.active {
  color: #00aeec;
}

.nav-link:hover {
  color: #00aeec;
}
</style>
