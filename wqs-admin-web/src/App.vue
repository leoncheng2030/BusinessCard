<template>
	<a-config-provider :locale="locale" :theme="themeConfig">
		<div :class="['app-wrapper', { 'gray-mode': grayModeOpen }]">
			<a-watermark
				:content="loginUserWatermarkOpen && userInfo ? [userInfo.name, userInfo.account] : undefined"
				class="admin-ui-main"
			>
				<router-view />
			</a-watermark>
		</div>
	</a-config-provider>
</template>

<script setup name="App">
	import { onUnmounted } from 'vue'
	import i18n from '@/locales'
	import { globalStore } from '@/store'
	import { theme } from 'ant-design-vue'
	const store = globalStore()
	store.initTheme()
	const locale = i18n.global.messages.value[i18n.global.locale.value].lang
	// 获取用户信息
	const userInfo = computed(() => store.userInfo)
	// 是否真暗黑
	const isRealDark = computed(() => store.theme === 'realDark')
	// 侧边菜单主题：realDark 视同 dark
	const sideTheme = computed(() => (store.theme === 'realDark' ? 'dark' : store.theme))
	// 整体主题：亮蓝主色 + 深色侧边栏 #282c34 + 白顶栏 + 浅灰内容底
	const themeConfig = computed(() => {
		const menuDarkBg = isRealDark.value ? '#141414' : '#282c34'
		const menuDarkSubBg = isRealDark.value ? '#1d1d1d' : '#23272e'
		return {
			algorithm: isRealDark.value ? theme.darkAlgorithm : theme.defaultAlgorithm,
			token: {
				colorPrimary: store.themeColor,
				borderRadius: roundedCornerStyleOpen.value ? 6 : 2
			},
			components: {
				Layout: {
					bodyBg: isRealDark.value ? '#141414' : '#f0f2f5',
					headerBg: isRealDark.value ? '#1d1d1d' : '#ffffff',
					siderBg: sideTheme.value === 'light' ? '#ffffff' : menuDarkBg,
					triggerBg: sideTheme.value === 'light' ? '#ffffff' : menuDarkBg
				},
				Menu: {
					darkItemBg: menuDarkBg,
					darkSubMenuItemBg: menuDarkSubBg,
					darkItemColor: 'rgba(255, 255, 255, 0.85)',
					darkItemHoverBg: 'rgba(0, 0, 0, 0.2)',
					darkItemSelectedBg: store.themeColor,
					darkItemSelectedColor: '#ffffff'
				},
				Table: {
					headerBg: isRealDark.value ? '#1d1d1d' : '#f2f6ff',
					headerColor: isRealDark.value ? 'rgba(255, 255, 255, 0.85)' : '#606266'
				}
			}
		}
	})
	// 水印开关
	const loginUserWatermarkOpen = computed(() => store.loginUserWatermarkOpen)
	// 圆角风格
	const roundedCornerStyleOpen = computed(() => store.roundedCornerStyleOpen)
	// 灰色模式
	const grayModeOpen = computed(() => store.grayModeOpen)
	// 灰色模式类名
	const GRAY_MODE_CLASS = 'gray-mode'
	// 监听灰色模式，同步到 html 元素
	watch(
		grayModeOpen,
		(isEnabled) => {
			document.documentElement.classList.toggle(GRAY_MODE_CLASS, isEnabled)
		},
		{ immediate: true }
	)
	// 组件卸载时清理
	onUnmounted(() => {
		document.documentElement.classList.remove(GRAY_MODE_CLASS)
	})
</script>

<style lang="less" scoped>
	.app-wrapper {
		width: 100%;
		height: 100%;
	}
</style>
