<template>
  <uni-popup ref="message" type="message">
    <uni-popup-message
      :type="msgType"
      :message="messageText"
      :duration="duration"
    >
    </uni-popup-message>
  </uni-popup>
</template>

<script>
export default {
  name: "Message",
  data() {
    return {
      msgType: "success",
      messageText: "",
      duration: 2000,
    };
  },
  methods: {
    success(messageText) {
      this.msgType = "success";
      this.messageText = messageText;

      this.$refs.message.open();
    },
    error(messageText) {
      this.msgType = "error";
      this.messageText = messageText;

      this.$refs.message.open();
    },
    warn(messageText) {
      this.msgType = "warn";
      this.messageText = messageText;

      this.$refs.message.open();
    },
  },
};
</script>