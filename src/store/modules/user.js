const defaultAvatar = "http://113.45.69.13:9000/image/default-avatar.jpg";

const defaultUserInfo = {
  avatar: defaultAvatar,
  username: "未登录",
  level: 0,
  exp: 0,
  nextExp: 0,
  coin: 0,
  bcoin: 0,
  following: 0,
  fans: 0,
  dynamic: 0,
};

const state = {
  token: localStorage.getItem("token") || "",
  userInfo: JSON.parse(localStorage.getItem("userInfo")) || defaultUserInfo,
};

const getters = {
  isLogin: (state) => !!state.token && !!state.userInfo,
  userInfo: (state) => state.userInfo || defaultUserInfo,
  avatar: (state) => state.userInfo.avatar || defaultAvatar,
};

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token;
    localStorage.setItem("token", token);
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo;
    localStorage.setItem("userInfo", JSON.stringify(userInfo));
  },
  LOGOUT(state) {
    state.token = "";
    state.userInfo = defaultUserInfo;
    localStorage.removeItem("token");
    localStorage.removeItem("userInfo");
  },
};


const actions = {
  login({ commit }, { token, userInfo }) {
    commit("SET_TOKEN", token);
    commit("SET_USER_INFO", userInfo);
  },
  logout({ commit }) {
    commit("LOGOUT");
    location.reload(); // 可选：强制刷新页面
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
