<template>
	<xn-panel :padding="16">
		<template #title>
			<span>APP 配置（供 C 端展示消费）</span>
		</template>
		<show-form category="CLIENT_SHOW_APP" />
	</xn-panel>
</template>

<script setup name="clientShowApp">
	import ShowForm from './showForm.vue'
</script>
