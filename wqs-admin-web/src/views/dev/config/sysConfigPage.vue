<template>
	<xn-panel :padding="16">
		<template #title>
			<span>系统配置</span>
		</template>
		<SysConfig />
	</xn-panel>
</template>

<script setup name="devConfigSys">
	import SysConfig from './sysConfig.vue'
</script>
