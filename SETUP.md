This project was built using a local LLM, using:
- LM Studio (to run the model)
  - Running [qwen3.6-35b-a3b-ud-mlx@4bit](https://huggingface.co/unsloth/Qwen3.6-27B-UD-MLX-4bit)
- Using [Crush](https://github.com/charmbracelet/crush) as the coding harness

The following `~/.config/crush/crush.json` configuration is used to get Crush to use LM Studio:
```
  ...
  "providers": {
    "lmstudio": {
      "name": "LM Studio",
      "base_url": "http://192.168.1.123:1234/v1/",
      "type": "openai",
      "models": [
        {
          "name": "qwen3.6-35b-a3b@4bit",
          "id": "qwen3.6-35b-a3b-ud-mlx@4bit",
          "context_window": 98304,
          "default_max_tokens": 8192
        }
      ]
    }
  },
  ...
```
