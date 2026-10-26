<template>
	<xn-panel :padding="16">
		<template #title>
			<span>其他配置</span>
		</template>
		<OtherConfig />
	</xn-panel>
</template>

<script setup name="devConfigOther">
	import OtherConfig from './otherConfig/index.vue'
</script>
