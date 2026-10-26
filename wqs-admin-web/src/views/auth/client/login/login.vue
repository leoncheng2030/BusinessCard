<template>
	<div class="login-container">
		<div class="login-content">
			<!-- 左侧品牌展示区 -->
			<div class="login-left">
				<div class="brand-info">
					<img :src="sysBaseConfig.SNOWY_SYS_LOGO" class="brand-logo" alt="Logo" />
					<h1 class="brand-title">{{ sysBaseConfig.SNOWY_SYS_NAME }}</h1>
					<p class="brand-desc">{{ sysBaseConfig.SNOWY_SYS_DEFAULT_DESCRRIPTION }}</p>
				</div>
				<div class="brand-footer">
					<p>{{ sysBaseConfig.SNOWY_SYS_COPYRIGHT }}</p>
				</div>
			</div>

			<!-- 右侧登录表单区 -->
			<div class="login-right">
				<div class="login-form-wrapper">
					<div class="login-header">
						<h2>欢迎登录</h2>
						<p>请使用您的账号和密码进行登录</p>
					</div>

					<a-tabs v-model:activeKey="activeKey" class="login-tabs">
						<a-tab-pane key="userAccount" tab="账号登录">
							<a-form ref="loginForm" :model="ruleForm" :rules="rules" layout="vertical">
								<a-form-item name="account" label="账号">
									<a-input v-model:value="ruleForm.account" placeholder="请输入账号" size="large" @keyup.enter="login">
										<template #prefix>
											<UserOutlined class="field-icon" />
										</template>
									</a-input>
								</a-form-item>
								<a-form-item name="password" label="密码">
									<a-input-password
										v-model:value="ruleForm.password"
										placeholder="请输入密码"
										size="large"
										autocomplete="off"
										@keyup.enter="login"
									>
										<template #prefix>
											<LockOutlined class="field-icon" />
										</template>
									</a-input-password>
								</a-form-item>
								<a-form-item name="validCode" v-if="captchaOpen === 'true'" label="验证码">
									<a-row :gutter="12">
										<a-col :span="15">
											<a-input
												v-model:value="ruleForm.validCode"
												placeholder="请输入验证码"
												size="large"
												@keyup.enter="login"
											>
												<template #prefix>
													<SafetyOutlined class="field-icon" />
												</template>
											</a-input>
										</a-col>
										<a-col :span="9">
											<div class="captcha-img-wrapper">
												<img
													:src="validCodeBase64"
													class="login-validCode-img"
													@click="loginCaptcha"
													title="点击刷新验证码"
												/>
											</div>
										</a-col>
									</a-row>
								</a-form-item>

								<div class="form-options">
									<a-checkbox v-model:checked="ruleForm.autologin">记住我</a-checkbox>
									<div class="links">
										<a href="/front/client/findPwd" class="forgot-link">忘记密码？</a>
									</div>
								</div>

								<a-form-item class="submit-item">
									<a-button
										type="primary"
										class="login-button"
										:loading="loading"
										size="large"
										@click="login"
										:disabled="loginButtonDisable"
										block
									>
										立即登录
									</a-button>
								</a-form-item>

								<div class="register-footer" v-if="registerOpen === 'true'">
									还没有账号？ <a href="/front/client/register">立即注册</a>
								</div>
							</a-form>
						</a-tab-pane>

						<a-tab-pane key="userSms" tab="手机登录" force-render v-if="phoneLogin === 'true'">
							<phone-login-form />
						</a-tab-pane>

						<a-tab-pane key="userEmail" tab="邮箱登录" force-render v-if="emailLogin === 'true'">
							<email-login-form />
						</a-tab-pane>
					</a-tabs>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup>
	import clientLoginApi from '@/api/auth/client/clientLoginApi'
	import smCrypto from '@/utils/smCrypto'
	import { required } from '@/utils/formRules'
	import { afterLogin } from './util'
	import configData from '@/config'
	import tool from '@/utils/tool'
	import { globalStore } from '@/store'
	import { loadAuthPageConfig } from '@/utils/brandConfig'
	const PhoneLoginForm = defineAsyncComponent(() => import('./phoneLoginForm.vue'))
	const EmailLoginForm = defineAsyncComponent(() => import('./emailLoginForm.vue'))
	const activeKey = ref('userAccount')
	const captchaOpen = ref(configData.SYS_BASE_CONFIG.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_C)
	const registerOpen = ref('false')
	const phoneLogin = ref('false')
	const emailLogin = ref('false')
	const validCodeBase64 = ref('')
	const loading = ref(false)
	const loginButtonDisable = ref(false)

	const ruleForm = reactive({
		validCode: '',
		validCodeReqNo: '',
		autologin: false
	})

	const rules = reactive({
		account: [required('请输入账号', 'blur')],
		password: [required('请输入密码', 'blur')]
	})
	const config = ref({
		theme: tool.data.get('APP_THEME') || 'default'
	})

	const store = globalStore()

	const sysBaseConfig = computed(() => {
		return store.sysBaseConfig
	})

	onMounted(() => {
		getSysConfig()
		// 加载记住的账号
		const rememberAccount = tool.data.get('REMEMBER_ACCOUNT')
		if (rememberAccount) {
			ruleForm.account = rememberAccount
			ruleForm.autologin = true
		}
	})
	// 加载系统开关与平台品牌（验证码/注册/登录方式等开关）
	const getSysConfig = () => {
		loadAuthPageConfig().then((data) => {
			loginButtonDisable.value = false
			captchaOpen.value = data.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_C
			registerOpen.value = data.SNOWY_SYS_DEFAULT_ALLOW_REGISTER_FLAG_FOR_C
			phoneLogin.value = data.SNOWY_SYS_DEFAULT_ALLOW_PHONE_LOGIN_FLAG_FOR_C
			emailLogin.value = data.SNOWY_SYS_DEFAULT_ALLOW_EMAIL_LOGIN_FLAG_FOR_C
			refreshSwitch()
		})
	}
	// 主题
	watch(
		() => config.value.theme,
		(newValue) => {
			document.body.setAttribute('data-theme', newValue)
		}
	)
	// 通过开关加载内容
	const refreshSwitch = () => {
		// 判断是否开启验证码
		if (captchaOpen.value === 'true') {
			// 加载验证码
			loginCaptcha()
			// 加入校验
			rules.validCode = [required('请输入验证码', 'blur')]
		}
	}
	// 获取验证码
	const loginCaptcha = () => {
		clientLoginApi.clientGetPicCaptcha().then((data) => {
			validCodeBase64.value = data.validCodeBase64
			ruleForm.validCodeReqNo = data.validCodeReqNo
			// 如果有输入的将其清空
			ruleForm.validCode = undefined
		})
	}
	//登陆
	const loginForm = ref()
	const login = async () => {
		loginForm.value
			.validate()
			.then(async () => {
				loading.value = true
				const loginData = {
					account: ruleForm.account,
					// 密码进行SM2加密，传输过程中看到的只有密文，后端存储使用hash
					password: smCrypto.doSm2Encrypt(ruleForm.password),
					validCode: ruleForm.validCode,
					validCodeReqNo: ruleForm.validCodeReqNo
				}
				// 获取token
				try {
					const loginToken = await clientLoginApi.clientLogin(loginData)
					// 记住账号
					if (ruleForm.autologin) {
						tool.data.set('REMEMBER_ACCOUNT', ruleForm.account)
					} else {
						tool.data.remove('REMEMBER_ACCOUNT')
					}
					await afterLogin(loginToken)
				} catch (err) {
					if (captchaOpen.value === 'true') {
						loginCaptcha()
					}
				}
			})
			.finally(() => {
				loading.value = false
			})
	}
</script>
<style lang="less" scoped>
	@import 'login';
</style>
