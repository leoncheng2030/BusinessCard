<template>
	<xn-panel :padding="16">
		<template #title>
			<span>微信小程序配置（供 C 端展示消费）</span>
		</template>
		<show-form category="CLIENT_SHOW_MINI" />
	</xn-panel>
</template>

<script setup name="clientShowMini">
	import ShowForm from './showForm.vue'
</script>
