<template>
	<xn-panel :padding="16">
		<template #title>
			<span>推送配置</span>
		</template>
		<PushConfig />
	</xn-panel>
</template>

<script setup name="devConfigPush">
	import PushConfig from './pushConfig/index.vue'
</script>
