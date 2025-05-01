// 导入 Vue Router 相关方法
import { createRouter, createWebHashHistory } from 'vue-router'
// 导入所有视图组件
import HomeView from '../views/HomeView.vue'
import AnimeView from '@/views/AnimeView.vue'
import CaricatureView from '@/views/CaricatureView.vue'
import DownLoadView from '@/views/DownLoadView.vue'
import EventsView from '@/views/EventsView.vue'
import GameCenterView from '@/views/GameCenterView.vue'
import BroadcastView from '@/views/BroadcastView.vue'
import MemberView from '@/views/MemberView.vue'
import PurchaseView from '@/views/PurchaseView.vue'
import PddView from '@/views/PddView.vue'
import NewSongsView from '@/views/NewSongsView.vue'
import CommunityCentersView from '@/views/CommunityCentersView.vue'
import ClassroomView from '@/views/ClassroomView.vue'
import ActivityView from '@/views/ActivityView.vue'
import ColumnView from '@/views/ColumnView.vue'
import MovieView from '@/views/MovieView.vue';
import OriginalView from '@/views/OriginalView.vue';
import DramaView from '@/views/DramaView.vue';
import ShowView from '@/views/ShowView.vue';
import DocView from '@/views/DocView.vue';
import CartoonView from '@/views/CartoonView.vue';
import GameView from '@/views/GameView.vue';
import RemixView from '@/views/RemixView.vue';
import MusicView from '@/views/MusicView.vue';
import DanceView from '@/views/DanceView.vue';
import FunView from '@/views/FunView.vue';
import StudyView from '@/views/StudyView.vue';
import TechView from '@/views/TechView.vue';
import NewsView from '@/views/NewsView.vue';
import FoodView from '@/views/FoodView.vue';
import StageView from '@/views/StageView.vue';
import AutoView from '@/views/AutoView.vue';
import StyleView from '@/views/StyleView.vue';
import SportView from '@/views/SportView.vue';
import PetView from '@/views/PetView.vue';
import VlogView from '@/views/VlogView.vue';
import MoreView from '@/views/MoreView.vue';
import UserView from '@/views/UserView.vue';
import VipView from '@/views/VipView.vue';
import MessageView from '@/views/MessageView.vue';
import CollectionView from '@/views/CollectionView.vue';
import HistoryView from '@/views/HistoryView.vue';
import CreateView from '@/views/CreateView.vue';
import VideoView from '@/views/VideoView.vue';
import HotView from '@/views/HotView.vue';
import SearchView from '@/views/SearchView.vue';
import UpLoadView from '@/views/UpLoadView.vue';
import DataView from '@/views/DataView.vue';
// 定义路由配置数组
const routes = [
  // 首页路由
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  // 动漫页面路由
  {
    path: '/Anime',
    name: 'AnimeView',
    component: AnimeView
  },
  // 漫画页面路由
  {
    path: '/Caricature',
    name: 'CaricatureView',
    component: CaricatureView,
  },
  // 下载页面路由
  {
    path: '/DownLoad',
    name: 'DownLoadView',
    component: DownLoadView,
  },
  // 活动页面路由
  {
    path: '/Events',
    name: 'EventsView',
    component: EventsView,
  },
  // 游戏中心页面路由
  {
    path: '/GameCenter',
    name: 'GameCenterView',
    component: GameCenterView,
  },
  // 直播页面路由
  {
    path: '/Broadcast',
    name: 'BroadcastView',
    component: BroadcastView,
  },
  // 会员页面路由
  {
    path: '/Member',
    name: 'MemberView',
    component: MemberView,
  },
  // 购买页面路由
  {
    path: '/Purchase',
    name: 'PurchaseView',
    component: PurchaseView,
  },
  // 拼多多页面路由
  {
    path:'/Pdd',
    name:'PddView',
    component:PddView
  },
  // 新歌页面路由
  {
    path:'/NewSongs',
    name:'NewSongsView',
    component:NewSongsView
  },
  // 社区中心页面路由
  {
    path:'/CommunityCenters',
    name:'CommunityCentersView',
    component:CommunityCentersView
  },
  // 课堂页面路由
  {
    path:'/Classroom',
    name:'ClassroomView',
    component:ClassroomView
  },
  // 活动页面路由
  {
    path:'/Activity',
    name:'ActivityView',
    component:ActivityView
  },
  // 专栏页面路由
  {
    path:'/Column',
    name:'ColumnView',
    component:ColumnView
  },

  // 电影页面路由
  { path: '/Movie', name: 'Movie', component: MovieView },
  // 原创页面路由
  { path: '/Original', name: 'Original', component: OriginalView },
  // 电视剧页面路由
  { path: '/Drama', name: 'Drama', component: DramaView },
  // 综艺页面路由
  { path: '/Show', name: 'Show', component: ShowView },
  // 纪录片页面路由
  { path: '/Doc', name: 'Doc', component: DocView },
  // 动画页面路由
  { path: '/Cartoon', name: 'Cartoon', component: CartoonView },
  // 游戏页面路由
  { path: '/Game', name: 'Game', component: GameView },
  // 鬼畜页面路由
  { path: '/Remix', name: 'Remix', component: RemixView },
  // 音乐页面路由
  { path: '/Music', name: 'Music', component: MusicView },
  // 舞蹈页面路由
  { path: '/Dance', name: 'Dance', component: DanceView },
  // 搞笑页面路由
  { path: '/Fun', name: 'Fun', component: FunView },
  // 学习页面路由
  { path: '/Study', name: 'Study', component: StudyView },
  // 科技页面路由
  { path: '/Tech', name: 'Tech', component: TechView },
  // 新闻页面路由
  { path: '/News', name: 'News', component: NewsView },
  // 美食页面路由
  { path: '/Food', name: 'Food', component: FoodView },
  // 舞台页面路由
  { path: '/Stage', name: 'Stage', component: StageView },
  // 汽车页面路由
  { path: '/Auto', name: 'Auto', component: AutoView },
  // 时尚页面路由
  { path: '/Style', name: 'Style', component: StyleView },
  // 运动页面路由
  { path: '/Sport', name: 'Sport', component: SportView },
  // 宠物页面路由
  { path: '/Pet', name: 'Pet', component: PetView },
  // Vlog页面路由
  { path: '/Vlog', name: 'Vlog', component: VlogView },
  // 更多页面路由
  { path: '/More', name: 'More', component: MoreView },
  // 用户页面路由
  {
    path: '/user',
    name: 'UserView',
    component: UserView
  },
  // VIP页面路由
  {
    path: '/vip',
    name: 'VipView',
    component: VipView
  },
  // 消息页面路由
  {
    path: '/message',
    name: 'MessageView',
    component: MessageView
  },
  // 活动页面路由
  {
    path: '/activity',
    name: 'ActivityView',
    component: ActivityView
  },
  // 收藏页面路由
  {
    path: '/collection',
    name: 'CollectionView',
    component: CollectionView
  },
  // 历史记录页面路由
  {
    path: '/history',
    name: 'HistoryView',
    component: HistoryView
  },
  // 创作中心页面路由
  {
    path: '/create',
    name: 'CreateView',
    component: CreateView
  },
  // 视频详情页面路由
  {
    path: '/video/:id',  // 使用动态路由来接收视频的 id
    name: 'VideoView',
    component: VideoView,
    props: true,  // 允许将 `id` 作为 props 传递给组件
  },

  // 热门页面路由
  {
    path: '/hot',
    name: 'HotView',
    component: HotView,
  },

  // 搜索页面路由
  {
    path: '/search',
    name: 'SearchView',
    component: SearchView,
  },
  // 上传页面路由
  {
    path: '/upload',
    name: 'UpLoadView',
    component: UpLoadView,
  },
  // 数据统计页面路由
  {
    path: '/data',
    name: 'DataView',
    component: DataView,
  },
]

// 创建路由实例
const router = createRouter({
  // 使用 hash 模式的路由历史
  history: createWebHashHistory(),
  // 配置路由表
  routes
})

// 导出路由实例
export default router
