export function actionToObject (json) {
  try {
    return JSON.parse(json)
  } catch (e) {
    console.log('err', e.message)
  }
  return []
}

export function getAclList (userAcl, type) {
  return userAcl[type]
}
