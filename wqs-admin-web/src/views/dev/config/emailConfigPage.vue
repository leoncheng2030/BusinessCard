<template>
	<xn-panel :padding="16">
		<template #title>
			<span>邮件配置</span>
		</template>
		<a-tabs v-model:activeKey="activeKey">
			<a-tab-pane key="channel" tab="服务商配置">
				<EmailConfig />
			</a-tab-pane>
			<a-tab-pane key="template" tab="邮件模板">
				<EmailTemplateConfig />
			</a-tab-pane>
		</a-tabs>
	</xn-panel>
</template>

<script setup name="devConfigEmail">
	import EmailConfig from './emailConfig/index.vue'
	import EmailTemplateConfig from './emailTemplateConfig/index.vue'
	// 默认停靠服务商配置，模板为辅
	const activeKey = ref('channel')
</script>
