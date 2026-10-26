<template>
	<LoginLayout :title="$t('login.forgetPassword')">
		<a-tabs v-model:activeKey="activeKey" class="login-tabs">
			<a-tab-pane key="userPhone" :tab="$t('login.restPhoneType')">
				<phone-find-form />
			</a-tab-pane>
			<a-tab-pane key="userEmail" :tab="$t('login.restEmailType')" force-render>
				<email-find-form />
			</a-tab-pane>
		</a-tabs>
	</LoginLayout>
</template>

<script setup>
	import LoginLayout from '../login/LoginLayout.vue'
	import PhoneFindForm from './phoneFindForm.vue'
	import EmailFindForm from './emailFindForm.vue'
	import { loadAuthPageConfig } from '@/utils/brandConfig'

	const activeKey = ref('userPhone')

	onMounted(() => {
		// 补拉平台品牌，LoginLayout品牌展示消费store
		loadAuthPageConfig({ includeSwitch: false })
	})
</script>

<style lang="less" scoped>
	@import '../login/login';
</style>
