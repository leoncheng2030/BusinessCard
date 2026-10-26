import configApi from '@/api/dev/configApi'
import configData from '@/config'
import tool from '@/utils/tool'
import { globalStore } from '@/store'

/**
 * 平台品牌配置工具：品牌数据存放公共表 DEV_BRAND_CONFIG（全平台共享）
 *
 * <p>登录/注册/找回密码页免登读取 brandList 并合并进系统基础配置供全局展示
 * （LoginLayout/登录后布局/文档标题均消费 store.sysBaseConfig）；
 * 系统开关仍走 sysBaseList（DEV_CONFIG，原品牌配置行已废弃清理）。</p>
 */
/** 平台品牌配置key清单（公共表，与 DEV_BRAND_CONFIG 存储一致） */
export const PLATFORM_BRAND_KEY_LIST = [
	'SNOWY_SYS_LOGO',
	'SNOWY_SYS_NAME',
	'SNOWY_SYS_VERSION',
	'SNOWY_SYS_COPYRIGHT',
	'SNOWY_SYS_COPYRIGHT_URL',
	'SNOWY_SYS_DEFAULT_DESCRRIPTION'
]

/** 是否平台品牌key */
export const isPlatformBrandKey = (key) => PLATFORM_BRAND_KEY_LIST.includes(key)

/**
 * 获取平台品牌配置（公共表免登读取，全平台一致）
 *
 * @returns {Promise<Object|null>} 品牌 key→value 映射；接口异常或为空返回 null
 */
export const fetchPlatformBrand = () => {
	return configApi
		.configBrandList()
		.then((data) => {
			if (!data) {
				return null
			}
			const brand = {}
			data.forEach((item) => {
				if (isPlatformBrandKey(item.configKey)) {
					brand[item.configKey] = item.configValue
				}
			})
			return Object.keys(brand).length ? brand : null
		})
		.catch(() => null)
}

/**
 * 合并配置到系统基础配置并应用全局（storage + store），返回合并后的配置对象
 *
 * <p>底值优先级：静态默认 < 本地缓存历史 < 本次补丁；调用方把需要下发的配置段作为 patch 传入，
 * 各登录类页面共用此函数，避免重复写 storage/store。</p>
 */
export const mergeSysBaseConfig = (patch) => {
	const merged = {
		...configData.SYS_BASE_CONFIG,
		...(tool.data.get('SNOWY_SYS_BASE_CONFIG') || {}),
		...(patch || {})
	}
	tool.data.set('SNOWY_SYS_BASE_CONFIG', merged)
	globalStore().setSysBaseConfig(merged)
	return merged
}

/**
 * 加载登录类页面基础配置（登录/注册/找回密码页共用）
 *
 * <p>includeSwitch=true（默认）时先拉系统开关 sysBaseList（DEV_CONFIG），
 * 再以平台品牌公共表覆盖同名校验（品牌全平台唯一，历史 DEV_CONFIG 品牌行已废弃）；
 * includeSwitch=false 用于仅加载免登品牌的场景（验证码等开关维持默认）。</p>
 *
 * @param {Object} [options]
 * @param {boolean} [options.includeSwitch=true] 是否拉取系统开关
 * @returns {Promise<Object>} 合并后的配置对象（含静态默认值，永不 reject）
 */
export const loadAuthPageConfig = async ({ includeSwitch = true } = {}) => {
	const merged = {
		...configData.SYS_BASE_CONFIG,
		...(tool.data.get('SNOWY_SYS_BASE_CONFIG') || {})
	}
	if (includeSwitch) {
		const switchList = await configApi.configSysBaseList().catch(() => null)
		if (switchList) {
			switchList.forEach((item) => {
				merged[item.configKey] = item.configValue
			})
		}
	}
	const brand = await fetchPlatformBrand()
	if (brand) {
		Object.assign(merged, brand)
	}
	tool.data.set('SNOWY_SYS_BASE_CONFIG', merged)
	globalStore().setSysBaseConfig(merged)
	return merged
}
