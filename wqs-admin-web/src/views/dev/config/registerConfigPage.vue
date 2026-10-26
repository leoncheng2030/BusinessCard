<template>
	<xn-panel :padding="16">
		<template #title>
			<span>注册配置</span>
		</template>
		<RegisterConfig />
	</xn-panel>
</template>

<script setup name="devConfigRegister">
	import RegisterConfig from './registerConfig/index.vue'
</script>
