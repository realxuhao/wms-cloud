/**
 * 操作权限处理
 * Copyright (c) 2019 ruoyi
 */
import store from '@/store'

export function hasPermi(value) {
	const allPermission = '*:*:*'
	const permissions = store.getters && store.getters.permissions
	if (value && value instanceof Array && value.length > 0) {
		const permissionFlag = value

		const hasPermissions = permissions.some(permission => {
			return allPermission === permission || permissionFlag.includes(permission)
		})

		if (!hasPermissions) {
			return false
		} else {
			return true
		}
	} else {
		throw new Error(`请设置操作权限标签值`)
	}
}
