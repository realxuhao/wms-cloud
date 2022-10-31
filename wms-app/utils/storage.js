
const accessToken = "AuthenticationToken"

const getToken = () => {
  const token = uni.getStorageSync(accessToken);
  return token
}

const setToken = (token) => {
  uni.setStorageSync(accessToken,token)
}

const removeToken = () => {
  uni.removeStorageSync(accessToken)
}

export { getToken, setToken, removeToken }
