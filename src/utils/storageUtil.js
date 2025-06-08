export function getUserInfo() {
    const raw = localStorage.getItem("userInfo");
    return raw ? JSON.parse(raw) : null;
  }