<template>
	<xn-panel :padding="16">
		<template #title>
			<span>文件配置</span>
		</template>
		<FileConfig />
	</xn-panel>
</template>

<script setup name="devConfigFile">
	import FileConfig from './fileConfig/index.vue'
</script>
