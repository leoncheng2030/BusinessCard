<template>
	<LoginLayout :title="$t('login.signInTitle')">
		<template #extra>
			<div class="login_config">
				<a-dropdown>
					<global-outlined />
					<template #overlay>
						<a-menu>
							<a-menu-item
								v-for="item in lang"
								:key="item.value"
								:command="item"
								:class="{ selected: config.lang === item.value }"
								@click="configLang(item.value)"
							>
								{{ item.name }}
							</a-menu-item>
						</a-menu>
					</template>
				</a-dropdown>
			</div>
		</template>

		<a-tabs v-model:activeKey="activeKey" class="login-tabs">
			<a-tab-pane key="userAccount" :tab="$t('login.accountPassword')">
				<a-form ref="loginForm" :model="ruleForm" :rules="rules">
					<a-form-item name="account">
						<a-input
							v-model:value="ruleForm.account"
							:placeholder="$t('login.accountPlaceholder')"
							size="large"
							@keyup.enter="login"
						>
							<template #prefix>
								<UserOutlined class="field-icon" />
							</template>
						</a-input>
					</a-form-item>
					<a-form-item name="password">
						<a-input-password
							v-model:value="ruleForm.password"
							:placeholder="$t('login.PWPlaceholder')"
							size="large"
							autocomplete="off"
							@keyup.enter="login"
						>
							<template #prefix>
								<LockOutlined class="field-icon" />
							</template>
						</a-input-password>
					</a-form-item>
					<a-form-item name="validCode" v-if="captchaOpen === 'true'">
						<a-row :gutter="12">
							<a-col :span="15">
								<a-input
									v-model:value="ruleForm.validCode"
									:placeholder="$t('login.validPlaceholder')"
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
						<span></span>
						<div class="links">
							<a @click="goFindPwd" class="forgot-link">{{ $t('login.forgetPassword') }}？</a>
							<a @click="goRegister" v-if="registerOpen === 'true'" class="forgot-link register-link">
								{{ $t('login.notAccountPleaseRegister') }}
							</a>
						</div>
					</div>

					<a-form-item class="submit-item">
						<a-button type="primary" class="login-button" :loading="loading" size="large" @click="login" block>
							{{ $t('login.signIn') }}
						</a-button>
					</a-form-item>
				</a-form>
			</a-tab-pane>
			<a-tab-pane key="userSms" :tab="$t('login.phoneLogin')" force-render v-if="loginTypes.phoneLogin === 'true'">
				<phone-login-form />
			</a-tab-pane>
			<a-tab-pane key="userEmail" :tab="$t('login.emailLogin')" force-render v-if="loginTypes.emailLogin === 'true'">
				<email-login-form />
			</a-tab-pane>
			<a-tab-pane key="userOtp" :tab="$t('login.otpLogin')" force-render v-if="loginTypes.otpLogin === 'true'">
				<otp-login-form :captchaOpen="captchaOpen" />
			</a-tab-pane>
		</a-tabs>
	</LoginLayout>
</template>
<script setup>
	import LoginLayout from './LoginLayout.vue'
	import loginApi from '@/api/auth/loginApi'
	const PhoneLoginForm = defineAsyncComponent(() => import('./phoneLoginForm.vue'))
	const EmailLoginForm = defineAsyncComponent(() => import('./emailLoginForm.vue'))
	const OtpLoginForm = defineAsyncComponent(() => import('./otpLoginForm.vue'))
	import smCrypto from '@/utils/smCrypto'
	import { required } from '@/utils/formRules'
	import { afterLogin } from './util'
	import { loadAuthPageConfig } from '@/utils/brandConfig'
	import configData from '@/config'
	import tool from '@/utils/tool'
	import { globalStore, iframeStore, keepAliveStore, viewTagsStore } from '@/store'
	import router from '@/router'
	const { proxy } = getCurrentInstance()
	const activeKey = ref('userAccount')

	// 跳转找回密码
	const goFindPwd = () => {
		router.push('/findpwd')
	}
	// 跳转注册
	const goRegister = () => {
		router.push('/register')
	}

	const captchaOpen = ref(configData.SYS_BASE_CONFIG.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_B)
	const registerOpen = ref('false')
	const loginTypes = reactive({
		phoneLogin: 'false',
		emailLogin: 'false',
		otpLogin: 'false'
	})
	const validCodeBase64 = ref('')
	const loading = ref(false)

	const ruleForm = reactive({
		account: '',
		password: '',
		validCode: '',
		validCodeReqNo: '',
		autologin: false
	})

	const rules = reactive({
		account: [required(proxy.$t('login.accountError'), 'blur')],
		password: [required(proxy.$t('login.PWError'), 'blur')]
	})
	const lang = ref([
		{
			name: '简体中文',
			value: 'zh-cn'
		},
		{
			name: 'English',
			value: 'en'
		}
	])
	const config = ref({
		lang: tool.data.get('APP_LANG') || proxy.$CONFIG.LANG,
		theme: tool.data.get('APP_THEME') || 'default'
	})

	const kStore = keepAliveStore()
	const iStore = iframeStore()
	const vStore = viewTagsStore()

	const clearKeepLive = kStore.clearKeepLive
	const clearIframeList = iStore.clearIframeList
	const clearViewTags = vStore.clearViewTags

	// 加载系统开关与平台品牌（验证码/注册/登录方式等开关）
	const loadSysBaseConfig = () => {
		loadAuthPageConfig().then((data) => {
			captchaOpen.value = data.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_B
			registerOpen.value = data.SNOWY_SYS_DEFAULT_ALLOW_REGISTER_FLAG_FOR_B
			loginTypes.phoneLogin = data.SNOWY_SYS_DEFAULT_ALLOW_PHONE_LOGIN_FLAG_FOR_B
			loginTypes.emailLogin = data.SNOWY_SYS_DEFAULT_ALLOW_EMAIL_LOGIN_FLAG_FOR_B
			loginTypes.otpLogin = data.SNOWY_SYS_DEFAULT_ALLOW_OTP_LOGIN_FLAG_FOR_B
			refreshSwitch()
		})
	}
	onMounted(() => {
		// 加载系统开关与平台品牌，验证码等开关初始化
		loadSysBaseConfig()
	})

	onBeforeMount(() => {
		clearViewTags()
		clearKeepLive()
		clearIframeList()
	})

	// 监听语言
	watch(
		() => config.value.lang,
		(newValue) => {
			proxy.$i18n.locale = newValue
			tool.data.set('APP_LANG', newValue)
		}
	)
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
			rules.validCode = [required(proxy.$t('login.validError'), 'blur')]
		}
	}
	// 获取验证码
	const loginCaptcha = () => {
		loginApi.getPicCaptcha().then((data) => {
			validCodeBase64.value = data.validCodeBase64
			ruleForm.validCodeReqNo = data.validCodeReqNo
		})
	}
	// 登录
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
				const loginToken = await loginApi.login(loginData)
				await afterLogin(loginToken)
			})
			.catch((err) => {
				console.log(err)
				loading.value = false
				if (captchaOpen.value === 'true') {
					loginCaptcha()
				}
			})
	}
	const configLang = (key) => {
		config.value.lang = key
	}
</script>
<style lang="less" scoped>
	@import 'login';
</style>
