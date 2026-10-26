<template>
	<xn-panel :padding="16">
		<template #title>
			<span>登录配置</span>
		</template>
		<LoginConfig />
	</xn-panel>
</template>

<script setup name="devConfigLogin">
	import LoginConfig from './loginConfig/index.vue'
</script>
