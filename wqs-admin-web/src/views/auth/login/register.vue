<script setup>
	import { cloneDeep } from 'lodash-es'
	import { useRouter } from 'vue-router'
	import { message } from 'ant-design-vue'
	import LoginLayout from './LoginLayout.vue'
	import { required, rules } from '@/utils/formRules'
	import smCrypto from '@/utils/smCrypto'
	import loginApi from '@/api/auth/loginApi'
	import configData from '@/config'
	import { loadAuthPageConfig } from '@/utils/brandConfig'

	const { proxy } = getCurrentInstance()
	const router = useRouter()
	const isRegister = ref(false)
	const registerFormRef = ref()
	const registerButtonDisable = ref(false)
	const registerFormData = ref({
		account: '',
		password: '',
		newPassword: '',
		validCode: '',
		validCodeReqNo: ''
	})
	// 表单校验规则
	const formRules = ref({
		account: [required(proxy.$t('login.accountPlaceholder'))],
		password: [
			required(proxy.$t('login.PWPlaceholder')),
			{
				validator: (rule, value) => {
					if (value && registerFormData.value.newPassword && value !== registerFormData.value.newPassword) {
						return Promise.reject(proxy.$t('login.enteredPasswordsDiffer'))
					}
					return Promise.resolve()
				},
				trigger: ['change', 'blur']
			}
		],
		newPassword: [
			required(proxy.$t('login.enterAgainPassword')),
			{
				validator: (rule, value) => {
					if (value && registerFormData.value.password && value !== registerFormData.value.password) {
						return Promise.reject(proxy.$t('login.enteredPasswordsDiffer'))
					}
					return Promise.resolve()
				},
				trigger: ['change', 'blur']
			}
		]
	})
	const captchaOpen = ref(configData.SYS_BASE_CONFIG.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_B)
	const validCodeBase64 = ref('')

	onMounted(() => {
		registerButtonDisable.value = true
		// 再去查配置
		getSysConfig()
	})
	// 加载系统开关与平台品牌（验证码开关等）
	const getSysConfig = () => {
		loadAuthPageConfig().then((data) => {
			registerButtonDisable.value = false
			captchaOpen.value = data.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_B
			refreshSwitch()
		})
	}
	// 通过开关加载内容
	const refreshSwitch = () => {
		// 判断是否开启验证码
		if (captchaOpen.value === 'true') {
			// 加载验证码
			registerCaptcha()
			// 加入校验
			formRules.value.validCode = [required(proxy.$t('login.validError'), 'blur'), rules.lettersNum]
		}
	}

	// 点击注册按钮
	const submitRegister = () => {
		formRules.value.validCode = [required(proxy.$t('login.validError')), rules.lettersNum]
		registerFormRef.value
			.validate()
			.then(() => {
				registerButtonDisable.value = false
				isRegister.value = true
				const loginData = {
					account: registerFormData.value.account,
					// 密码进行SM2加密，传输过程中看到的只有密文，后端存储使用hash
					password: smCrypto.doSm2Encrypt(cloneDeep(registerFormData.value.password)),
					validCode: registerFormData.value.validCode,
					validCodeReqNo: registerFormData.value.validCodeReqNo
				}
				loginApi
					.register(loginData)
					.then(() => {
						router.replace({
							path: '/login'
						})
						message.success('注册成功')
					})
					.catch(() => {
						// 异常的时候一般可能就是验证码过期，所以我们将其刷新
						if (captchaOpen.value === 'true') {
							registerCaptcha()
						}
					})
					.finally(() => {
						isRegister.value = false
					})
			})
			.catch(() => {})
	}
	// 获取图形验证码
	const registerCaptcha = () => {
		loginApi.getPicCaptcha().then((data) => {
			validCodeBase64.value = data.validCodeBase64
			registerFormData.value.validCodeReqNo = data.validCodeReqNo
			// 如果有输入的将其清空
			registerFormData.value.validCode = undefined
		})
	}
	// 跳转登录
	const goLogin = () => {
		router.push('/login')
	}
</script>

<template>
	<LoginLayout :title="$t('login.userRegister')">
		<a-form ref="registerFormRef" :model="registerFormData" :rules="formRules" autocomplete="off">
			<a-form-item name="account">
				<a-input v-model:value="registerFormData.account" :placeholder="$t('login.accountPlaceholder')" size="large">
					<template #prefix>
						<UserOutlined class="field-icon" />
					</template>
				</a-input>
			</a-form-item>
			<a-form-item name="password">
				<a-input-password
					v-model:value="registerFormData.password"
					:placeholder="$t('login.PWPlaceholder')"
					size="large"
				>
					<template #prefix>
						<LockOutlined class="field-icon" />
					</template>
				</a-input-password>
			</a-form-item>
			<a-form-item name="newPassword">
				<a-input-password
					v-model:value="registerFormData.newPassword"
					:placeholder="$t('login.enterAgainPassword')"
					size="large"
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
							v-model:value="registerFormData.validCode"
							:placeholder="$t('login.validError')"
							size="large"
							@keyup.enter="submitRegister"
						>
							<template #prefix>
								<SafetyOutlined class="field-icon" />
							</template>
						</a-input>
					</a-col>
					<a-col :span="9">
						<div class="captcha-img-wrapper">
							<img :src="validCodeBase64" class="login-validCode-img" @click="registerCaptcha" title="点击刷新验证码" />
						</div>
					</a-col>
				</a-row>
			</a-form-item>

			<a-form-item class="submit-item">
				<a-button
					type="primary"
					class="login-button"
					:loading="isRegister"
					size="large"
					@click="submitRegister"
					:disabled="registerButtonDisable"
					block
				>
					{{ $t('login.register') }}
				</a-button>
			</a-form-item>

			<div class="register-footer">
				{{ $t('login.haveAccountPleaseLogin') }} <a @click="goLogin">{{ $t('login.signIn') }}</a>
			</div>
		</a-form>
	</LoginLayout>
</template>

<style lang="less" scoped>
	@import 'login';
</style>
