<template>
	<xn-panel :padding="16">
		<template #title>
			<span>微信公众号配置（供 C 端展示消费）</span>
		</template>
		<show-form category="CLIENT_SHOW_MP" />
	</xn-panel>
</template>

<script setup name="clientShowMp">
	import ShowForm from './showForm.vue'
</script>
