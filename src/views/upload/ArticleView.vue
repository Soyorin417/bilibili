<template>
  <div class="article-view">
    <VideoBar />
    <div class="container">
      <DataNav currentPage="upload" />
      <div class="main-content">
        <div class="form-header">
          <h2>专栏投稿</h2>
          <button class="cancel-btn" @click="resetForm">
            <i class="bi bi-x"></i>
            取消投稿
          </button>
        </div>

        <div class="form-content">
          <div class="form-section">
            <h3>基本信息</h3>
            <div class="form-group">
              <label>标题 <span class="required">*</span></label>
              <input
                type="text"
                v-model="articleInfo.title"
                placeholder="请输入文章标题"
                maxlength="80"
              />
              <span class="char-count">{{ articleInfo.title.length }}/80</span>
            </div>

            <div class="form-group">
              <label>分类 <span class="required">*</span></label>
              <select v-model="articleInfo.category">
                <option value="">请选择分类</option>
                <option value="tech">科技</option>
                <option value="game">游戏</option>
                <option value="anime">动漫</option>
                <option value="music">音乐</option>
                <option value="life">生活</option>
              </select>
            </div>

            <div class="form-group">
              <label>标签</label>
              <div class="tags-input">
                <div class="tag-list">
                  <span v-for="(tag, index) in articleInfo.tags" :key="index" class="tag">
                    {{ tag }}
                    <i class="bi bi-x" @click="removeTag(index)"></i>
                  </span>
                </div>
                <input
                  type="text"
                  v-model="tagInput"
                  placeholder="输入标签，按回车添加"
                  @keydown.enter.prevent="addTag"
                />
              </div>
            </div>

            <div class="form-group">
              <label>简介</label>
              <textarea
                v-model="articleInfo.description"
                placeholder="填写文章简介，让更多人了解你的文章"
                rows="4"
              ></textarea>
            </div>
          </div>

          <div class="form-section">
            <h3>封面设置</h3>
            <div class="cover-upload">
              <div class="cover-preview" v-if="articleInfo.cover">
                <img :src="articleInfo.cover" alt="文章封面" />
                <div class="cover-actions">
                  <button @click="removeCover"><i class="bi bi-trash"></i></button>
                </div>
              </div>
              <div class="cover-upload-box" v-else @click="triggerCoverInput">
                <i class="bi bi-image"></i>
                <p>点击上传封面</p>
                <input
                  type="file"
                  ref="coverInput"
                  @change="handleCoverSelect"
                  accept="image/*"
                  class="file-input"
                />
              </div>
            </div>
          </div>

          <div class="form-section">
            <h3>文章内容 <span class="required">*</span></h3>
            <div class="editor-container">
              <div class="editor-toolbar">
                <button @click="formatText('bold')" title="加粗">
                  <i class="bi bi-type-bold"></i>
                </button>
                <button @click="formatText('italic')" title="斜体">
                  <i class="bi bi-type-italic"></i>
                </button>
                <button @click="formatText('underline')" title="下划线">
                  <i class="bi bi-type-underline"></i>
                </button>
                <div class="separator"></div>
                <button @click="formatText('h1')" title="标题1">
                  <i class="bi bi-type-h1"></i>
                </button>
                <button @click="formatText('h2')" title="标题2">
                  <i class="bi bi-type-h2"></i>
                </button>
                <button @click="formatText('h3')" title="标题3">
                  <i class="bi bi-type-h3"></i>
                </button>
                <div class="separator"></div>
                <button @click="formatText('ul')" title="无序列表">
                  <i class="bi bi-list-ul"></i>
                </button>
                <button @click="formatText('ol')" title="有序列表">
                  <i class="bi bi-list-ol"></i>
                </button>
                <button @click="formatText('blockquote')" title="引用">
                  <i class="bi bi-quote"></i>
                </button>
                <div class="separator"></div>
                <button @click="formatText('link')" title="插入链接">
                  <i class="bi bi-link-45deg"></i>
                </button>
                <button @click="formatText('image')" title="插入图片">
                  <i class="bi bi-image"></i>
                </button>
              </div>
              <div
                class="editor-content"
                contenteditable="true"
                @input="updateContent"
                v-html="articleInfo.content"
              ></div>
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button class="draft-btn">保存草稿</button>
          <button class="submit-btn">立即投稿</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import VideoBar from "@/components/navBar/VideoBar.vue";
import DataNav from "@/components/navBar/DataNav.vue";

export default {
  name: "ArticleView",
  components: {
    VideoBar,
    DataNav,
  },
  data() {
    return {
      articleInfo: {
        title: "",
        category: "",
        tags: [],
        description: "",
        cover: null,
        content: "",
      },
      tagInput: "",
    };
  },
  methods: {
    triggerCoverInput() {
      this.$refs.coverInput.click();
    },
    handleCoverSelect(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.articleInfo.cover = e.target.result;
        };
        reader.readAsDataURL(file);
      }
    },
    removeCover() {
      this.articleInfo.cover = null;
    },
    addTag() {
      if (this.tagInput.trim() && !this.articleInfo.tags.includes(this.tagInput.trim())) {
        this.articleInfo.tags.push(this.tagInput.trim());
        this.tagInput = "";
      }
    },
    removeTag(index) {
      this.articleInfo.tags.splice(index, 1);
    },
    updateContent(e) {
      this.articleInfo.content = e.target.innerHTML;
    },
    formatText(type) {
      const editor = document.querySelector(".editor-content");
      const selection = window.getSelection();
      console.log(selection);
      let url, imageUrl;

      switch (type) {
        case "bold":
          document.execCommand("bold", false, null);
          break;
        case "italic":
          document.execCommand("italic", false, null);
          break;
        case "underline":
          document.execCommand("underline", false, null);
          break;
        case "h1":
        case "h2":
        case "h3":
          document.execCommand("formatBlock", false, type);
          break;
        case "ul":
          document.execCommand("insertUnorderedList", false, null);
          break;
        case "ol":
          document.execCommand("insertOrderedList", false, null);
          break;
        case "blockquote":
          document.execCommand("formatBlock", false, "blockquote");
          break;
        case "link":
          url = prompt("请输入链接地址：");
          if (url) {
            document.execCommand("createLink", false, url);
          }
          break;
        case "image":
          imageUrl = prompt("请输入图片地址：");
          if (imageUrl) {
            document.execCommand("insertImage", false, imageUrl);
          }
          break;
      }

      editor.focus();
    },
    resetForm() {
      this.articleInfo = {
        title: "",
        category: "",
        tags: [],
        description: "",
        cover: null,
        content: "",
      };
      this.tagInput = "";
    },
  },
};
</script>

<style scoped>
.article-view {
  min-height: 100vh;
  background-color: #f4f4f4;
}

.container {
  max-width: 1600px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 20px;
}

.main-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 24px;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-header h2 {
  margin: 0;
  font-size: 20px;
}

.cancel-btn {
  background: none;
  border: none;
  color: #61666d;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.form-section {
  margin-bottom: 32px;
}

.form-section h3 {
  font-size: 16px;
  margin: 0 0 16px;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #18191c;
}

.required {
  color: #ff6b6b;
}

input[type="text"],
textarea,
select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea {
  resize: vertical;
}

.char-count {
  position: absolute;
  right: 8px;
  bottom: 8px;
  color: #9499a0;
  font-size: 12px;
}

.tags-input {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px;
  min-height: 40px;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 8px;
}

.tag {
  background: #f0f2f5;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.tag i {
  cursor: pointer;
}

.cover-upload {
  width: 240px;
}

.cover-preview {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
}

.cover-preview img {
  width: 100%;
  display: block;
}

.cover-actions {
  position: absolute;
  top: 8px;
  right: 8px;
}

.cover-actions button {
  background: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
}

.cover-upload-box {
  border: 2px dashed #ddd;
  border-radius: 4px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
}

.cover-upload-box i {
  font-size: 32px;
  color: #9499a0;
  margin-bottom: 8px;
}

.editor-container {
  border: 1px solid #ddd;
  border-radius: 4px;
}

.editor-toolbar {
  padding: 8px;
  border-bottom: 1px solid #ddd;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.editor-toolbar button {
  background: none;
  border: none;
  padding: 4px 8px;
  cursor: pointer;
  color: #61666d;
}

.editor-toolbar button:hover {
  background: #f0f2f5;
  border-radius: 4px;
}

.separator {
  width: 1px;
  background: #ddd;
  margin: 0 4px;
}

.editor-content {
  min-height: 400px;
  padding: 16px;
  outline: none;
}

.editor-content:empty:before {
  content: "请输入文章内容...";
  color: #9499a0;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #f4f4f4;
}

.draft-btn {
  padding: 12px 24px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn {
  padding: 12px 24px;
  background: #00aeec;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:hover {
  background: #0095cc;
}
</style>
