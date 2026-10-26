<template>
	<div class="login-container">
		<!-- 页面顶部Logo、标题与描述 -->
		<div class="login-page-header">
			<a
				class="brand-header"
				:class="{ 'no-link': !sysBaseConfig.SNOWY_SYS_COPYRIGHT_URL }"
				:href="sysBaseConfig.SNOWY_SYS_COPYRIGHT_URL"
				target="_blank"
				@click="handleLink"
			>
				<img :src="sysBaseConfig.SNOWY_SYS_LOGO" :alt="sysBaseConfig.SNOWY_SYS_NAME" />
				<span>{{ sysBaseConfig.SNOWY_SYS_NAME }}</span>
			</a>
			<p class="brand-desc" v-if="sysBaseConfig.SNOWY_SYS_DEFAULT_DESCRRIPTION">
				{{ sysBaseConfig.SNOWY_SYS_DEFAULT_DESCRRIPTION }}
			</p>
		</div>
		<div class="login-content">
			<!-- 左侧插画展示区 -->
			<div class="login-left">
				<img class="brand-illustration" src="/img/login_banner.png" alt="" />
			</div>

			<!-- 右侧表单区 -->
			<div class="login-right">
				<slot name="extra" />
				<div class="login-form-wrapper">
					<div class="login-header" v-if="title || subtitle">
						<h2 v-if="title">{{ title }}</h2>
						<p v-if="subtitle">{{ subtitle }}</p>
					</div>
					<slot />
				</div>
			</div>
		</div>
		<div class="login-footer">{{ sysBaseConfig.SNOWY_SYS_COPYRIGHT }} {{ sysBaseConfig.SNOWY_SYS_VERSION }}</div>
	</div>
</template>

<script setup>
	import { globalStore } from '@/store'

	defineProps({
		title: { type: String, default: '' },
		subtitle: { type: String, default: '' }
	})

	const store = globalStore()
	const sysBaseConfig = computed(() => {
		return store.sysBaseConfig
	})
	// logo链接
	const handleLink = (e) => {
		if (!sysBaseConfig.value.SNOWY_SYS_COPYRIGHT_URL) {
			e?.stopPropagation()
			e?.preventDefault()
		}
	}
</script>

<style lang="less" scoped>
	/* ========== 页面容器 ========== */
	.login-container {
		width: 100vw;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 4vh 16px 6vh;
		position: relative;
		overflow-x: hidden;
		overflow-y: auto;
		background: #1a1a2e;
		/* 固定定位铺满视口：既避免模糊边缘发虚，又不会给滚动容器制造额外可滚动区域（幽灵滚动条） */
		&::before {
			content: '';
			position: fixed;
			top: -10%;
			left: -10%;
			width: 120%;
			height: 120%;
			background-image: url(/img/login_background.png);
			background-size: cover;
			background-position: center;
			filter: blur(10px) saturate(120%);
			z-index: 0;
		}
	}

	/* ========== 页面顶部Logo、标题与描述 ========== */
	.login-page-header {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: auto;
		margin-bottom: 36px;
		position: relative;
		z-index: 1;

		.brand-header {
			display: flex;
			flex-direction: column;
			align-items: center;
			text-decoration: none;

			img {
				width: 72px;
				height: 72px;
				border-radius: 16px;
				margin-bottom: 16px;
				object-fit: cover;
			}

			span {
				font-size: 22px;
				font-weight: 600;
				color: #fff;
				letter-spacing: 2px;
				text-shadow: 0 2px 8px rgba(0, 0, 0, 0.25);
			}

			&.no-link {
				cursor: default;
			}
		}

		.brand-desc {
			margin: 14px 0 0;
			max-width: 480px;
			text-align: center;
			font-size: 15px;
			line-height: 1.8;
			color: rgba(255, 255, 255, 0.78);
			letter-spacing: 1px;
			text-shadow: 0 2px 8px rgba(0, 0, 0, 0.25);
		}
	}

	.login-content {
		display: flex;
		width: 1080px;
		min-height: 500px;
		margin-bottom: auto;
		border-radius: 20px;
		overflow: hidden;
		position: relative;
		z-index: 1;
		background: transparent;
		border: 1px solid rgba(255, 255, 255, 0.4);
		border-top-color: rgba(255, 255, 255, 0.6);
		border-left-color: rgba(255, 255, 255, 0.5);
		box-shadow:
			0 20px 60px rgba(0, 0, 0, 0.2),
			0 4px 16px rgba(0, 0, 0, 0.1),
			inset 0 1px 0 rgba(255, 255, 255, 0.35),
			inset 0 0 20px rgba(255, 255, 255, 0.05);
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: calc(100% - 460px);
			right: 0;
			height: 50%;
			background: linear-gradient(180deg, rgba(255, 255, 255, 0.15) 0%, transparent 100%);
			pointer-events: none;
			z-index: 0;
			border-radius: 20px 20px 0 0;
		}
	}

	/* ========== 左侧插画区（无背景色，插画铺满整个面板） ========== */
	.login-left {
		flex: 1;
		position: relative;
		overflow: hidden;

		/* 插画：图片已裁至画面实际范围，cover 铺满整个面板，过高页面仅居中轻微裁切两侧 */
		.brand-illustration {
			position: absolute;
			inset: 0;
			width: 100%;
			height: 100%;
			object-fit: cover;
			object-position: center;
			pointer-events: none;
			user-select: none;
			z-index: 1;
		}
	}

	/* ========== 页面底部版权 ========== */
	.login-footer {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 24px;
		z-index: 1;
		text-align: center;
		font-size: 13px;
		color: rgba(255, 255, 255, 0.65);
		text-shadow: 0 1px 2px rgba(0, 0, 0, 0.35);
	}

	/* ========== 右侧表单区 ========== */
	.login-right {
		width: 460px;
		padding: 40px 48px;
		display: flex;
		flex-direction: column;
		justify-content: center;
		background: linear-gradient(
			160deg,
			rgba(255, 255, 255, 0.82) 0%,
			rgba(255, 255, 255, 0.68) 50%,
			rgba(255, 255, 255, 0.78) 100%
		);
		border-left: 1px solid rgba(255, 255, 255, 0.3);
		position: relative;
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			height: 120px;
			background: linear-gradient(180deg, rgba(255, 255, 255, 0.3) 0%, transparent 100%);
			pointer-events: none;
		}

		.login-form-wrapper {
			width: 100%;
			position: relative;
			z-index: 1;
		}

		.login-header {
			margin-bottom: 24px;

			h2 {
				font-size: 24px;
				font-weight: 600;
				color: rgba(0, 0, 0, 0.85);
				margin-bottom: 8px;
			}

			p {
				color: rgba(0, 0, 0, 0.45);
				font-size: 14px;
			}
		}
	}

	/* ========== 响应式适配 ========== */
	@media (max-width: 1100px) {
		.login-content {
			width: 100%;
			max-width: 500px;
			height: auto;

			&::before {
				left: 0;
			}
		}
		.login-left {
			display: none;
		}
		.login-right {
			width: 100%;
			padding: 40px;
			background: linear-gradient(160deg, rgba(255, 255, 255, 0.85) 0%, rgba(255, 255, 255, 0.75) 100%);
		}
	}
</style>
