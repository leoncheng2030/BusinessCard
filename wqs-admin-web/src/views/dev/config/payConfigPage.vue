<template>
	<xn-panel :padding="16">
		<template #title>
			<span>支付配置</span>
		</template>
		<PayConfig />
	</xn-panel>
</template>

<script setup name="devConfigPay">
	import PayConfig from './payConfig/index.vue'
</script>
