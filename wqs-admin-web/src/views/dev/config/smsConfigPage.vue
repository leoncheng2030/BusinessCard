<template>
	<xn-panel :padding="16">
		<template #title>
			<span>短信配置</span>
		</template>
		<a-tabs v-model:activeKey="activeKey">
			<a-tab-pane key="channel" tab="服务商配置">
				<SmsConfig />
			</a-tab-pane>
			<a-tab-pane key="template" tab="短信模板">
				<SmsTemplateConfig />
			</a-tab-pane>
		</a-tabs>
	</xn-panel>
</template>

<script setup name="devConfigSms">
	import SmsConfig from './smsConfig/index.vue'
	import SmsTemplateConfig from './smsTemplateConfig/index.vue'
	// 默认停靠服务商配置，模板为辅
	const activeKey = ref('channel')
</script>
