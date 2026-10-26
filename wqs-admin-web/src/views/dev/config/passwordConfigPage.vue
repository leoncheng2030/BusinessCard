<template>
	<xn-panel :padding="16">
		<template #title>
			<span>密码配置</span>
		</template>
		<PasswordConfig />
	</xn-panel>
</template>

<script setup name="devConfigPassword">
	import PasswordConfig from './passwordConfig/index.vue'
</script>
